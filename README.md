# Post Service Assignment
## Overview

The application uses:
- Spring Boot
- PostgreSQL
- Redis
- Docker Compose

The system implements:
- Post creation
- Comment/reply system
- Virality score engine
- Redis-based concurrency guardrails
- Notification throttling
- Scheduled notification batching

---

# Tech Stack

- Java 17
- Spring Boot
- PostgreSQL
- Redis
- Docker
- Spring Data JPA
- Spring Data Redis

---

# Features

## REST APIs

### Create Post
POST /api/posts

### Add Comment
POST /api/posts/{postId}/comments

### Like Post
POST /api/posts/{postId}/like

---

# Database Design

## Entities

### User
- id
- username
- premium

### Bot
- id
- name
- personaDescription

### Post
- id
- authorId
- authorType
- content
- createdAt

### Comment
- id
- postId
- parentCommentId
- authorId
- authorType
- content
- depthLevel
- createdAt

---

# Redis Guardrails

## 1. Virality Engine

Redis key:
post:{id}:virality_score

Scoring:
- Bot Reply → +1
- Human Like → +20
- Human Comment → +50

Redis INCR operations are used for atomic updates.

---

## 2. Horizontal Cap

Redis key:
post:{id}:bot_count

Logic:
- Every bot comment increments Redis counter atomically.
- If count exceeds 100:
    - decrement rollback performed
    - request rejected with exception

This guarantees thread safety during concurrent requests.

---

## 3. Vertical Cap

The comment reply depth cannot exceed 20.

Logic:
- Parent comment depth checked before inserting reply.
- New depth = parent depth + 1.
- Reject if depth > 20.

---

## 4. Cooldown Cap

Redis key:
cooldown:bot:{botId}:human:{humanId}

Logic:
- Redis SETNX with TTL used.
- Prevents same bot interacting with same human within cooldown window.

---

# Notification Engine

## Immediate Notification Logic

Redis key:
notif:cooldown:user:{id}

If cooldown key absent:
- send notification immediately
- create cooldown key with TTL

Else:
- push message into Redis List

Redis list:
user:{id}:pending_notifications

---

## Scheduled Notification Sweeper

A Spring @Scheduled job runs every 5 minutes.

Logic:
- scans pending notification lists
- summarizes notifications
- clears Redis list

---

# Thread Safety & Concurrency

Thread safety is guaranteed using Redis atomic operations:
- INCR
- DECR
- SETNX
- TTL

No in-memory HashMap or static variables are used.

Redis acts as the centralized distributed guardrail layer.

PostgreSQL acts as the source of truth.

Database transactions commit only after Redis validations succeed.

---

# Docker Setup

## Start Application

docker compose up --build

## Stop Application

docker compose down



# Redis Usage

Used for:

* virality counters
* cooldown tracking
* bot caps
* notification batching
* atomic concurrency control

---

# Deliverables

* Spring Boot source code
* Docker Compose setup
* PostgreSQL integration
* Redis integration
* REST APIs
* Guardrail engine
* Notification engine

# Postman Collection

The repository contains a Postman collection file for API testing:

postserviceassignment.postman_collection.json

Import this collection into Postman to test:
- Create Post API
- Add Comment API
- Reply APIs
- Like Post API
  


# Asset Monitor

A small demo project simulating a report aggregation and alerting system, inspired by
concepts behind military systems (sharing/exchange of information between sources
and analysts). Built to get hands-on with a few technologies I hadn't used professionally:
XMPP messaging, Linux/VM operations, basic Azure exposure, and a DevSecOps pipeline.

All data, entities, and "reports" in this project are entirely fictional and generic
(vehicle/checkpoint status style data) — this is a learning exercise, not a real
intelligence or sensor system.

## What it does

Fictional "sources" (fake sensors/units) publish structured status reports over XMPP.
A Spring Boot backend receives these reports, applies a simple priority rule, and pushes
updates to an Angular frontend in real time over WebSocket. The frontend shows a live
aggregated feed and raises an alert for high-priority reports.

## Architecture

```
[Fake sources] --XMPP--> [XMPP server] --XMPP--> [Spring Boot backend] --WebSocket--> [Angular frontend]
```

- **Fake sources**: small scripts simulating sensors (e.g. unit-07, gate-B) publishing
  JSON reports over XMPP on a timer
- **XMPP server**: ejabberd (containerized), used as the messaging backbone between sources
  and the backend
- **Backend**: Spring Boot, connects to XMPP as a listening client (Smack library), persists
  reports, applies a priority rule, and pushes enriched reports to connected frontends via
  WebSocket (STOMP)
- **Frontend**: Angular, subscribes over WebSocket, displays a live feed table, and shows an
  alert toast for high-priority reports

## Why these technologies

This project exists specifically to build working familiarity with technologies 
that I don't yet have professional experience with:

- **XMPP**: used as the source-to-backend messaging protocol
- **Linux / virtualization**: entire stack runs on a self-managed Ubuntu VM (VirtualBox +
  Vagrant), provisioned declaratively rather than set up manually
- **Azure**: (planned) deploy a piece of the stack to Azure free tier
- **DevSecOps**: CI/CD pipeline with automated tests and dependency/security scanning

## Status

🚧 Work in progress — built incrementally as a learning project.

## Project structure

```
asset-monitor/
  backend/    Spring Boot application
  frontend/   Angular application
  docker-compose.yml   Runs XMPP server + backend + frontend together
```

## Running locally

See `docs/setup.md` for full details. Short version:

- **Local dev** (fast iteration): run backend with `mvn spring-boot:run` and frontend with
  `npm start`, no Docker/VM needed.
- **VM deployment test**: `vagrant up` creates the VM, installs Docker and Git, clones
  this repo, and starts it with Docker Compose -- all in one command.

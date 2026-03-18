# ToDoApp

ToDoApp is an offline-first Android task management application built to keep task data usable without internet access and synchronized when connectivity returns.

## Features

- Local persistence with Room
- Cloud synchronization with Firebase Firestore
- Offline to online synchronization flow
- Soft delete support
- Conflict resolution with version and updated-time comparison
- Task version tracking with a `TaskEvent` table
- MVVM-based architecture
- Dependency injection with Hilt

## Technologies

- Kotlin
- Android Studio
- Room
- Firebase Firestore
- Hilt
- MVVM

## Synchronization Logic

- Each task keeps `version` and `updatedTime` values
- Deleted records take priority during sync
- Higher version wins during conflict resolution
- If versions are equal, the more recent update time is used

## Purpose

This project was developed to demonstrate how offline-first behavior and client-side synchronization logic can be managed in a mobile application.

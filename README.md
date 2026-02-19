# Ecommerce-Backend-Project
✅ Sprint 1 – Implementation Details

In Sprint 1, I built the foundational backend for the e-commerce system with the following features:

1.**Database Design**

Designed a normalized database schema for Users (Admin & Customer), Products, Orders, OrderItems, and Payment.

Implemented entity relationships using Spring Data JPA.

Used H2 in-memory database for development and testing.

Layered Architecture Implementation

2.**Implemented a clean layered architecture:**

Controller → Service → Repository → Entity


3.Added a repository layer using JpaRepository to avoid direct database access.

4.**REST API Development**

Developed REST APIs for:

User registration (signup)

User login (signin)

User logout

Product management (Admin-only access)

Product listing (public access)

5.**Authentication & Authorization**

Implemented session-based authentication using Spring Security.

Implemented role-based authorization (ADMIN vs CUSTOMER).

Secured APIs using @PreAuthorize annotations.

Implemented session lifecycle management (login, session persistence, logout).

6.**Password Security**

Implemented password encryption using BCryptPasswordEncoder.

**7.API Documentation**

Integrated Swagger (OpenAPI) for API documentation and testing.

**8.Technology Stack**

Spring Boot (4.x)

Spring Security

Spring Data JPA

Java JDK 21

H2 Database

Maven
# Project Title: **Enterprise Issue Escalation & Service Desk Management System**

# 1. Project Overview

The Enterprise Issue Escalation & Service Desk Management System is a backend application developed using a Microservices Architecture.The system provides a structured process for handling complaints from the time they are created until they are resolved.

There are three main roles in the system:

* **User**
* **Admin**
* **Staff**

Each role has specific permissions and responsibilities.

# 2. Tools Used

| Tool                        | Purpose                        |
| --------------------------- | ------------------------------ |
| **Spring Tool Suite (STS)** | Backend code implementation    |
| **PostgreSQL**              | Database storage               |
| **Postman**                 | API testing                    |
| **GitHub**                  | Version control and deployment |

# 3. Microservices

The backend is divided into the following services:

### Auth Service

Handles:

* User sign-up
* User login
* JWT token generation
* User roles

### Complaint Service

Handles:

* Creating complaints
* Viewing complaints
* Updating complaint status
* Managing complaint information

### Assignment Service

Handles:

* Assigning complaints to departments
* Assigning complaints to staff
* Managing assignments

### Notification Service

Handles:

* User notifications
* Complaint-related updates
* Assignment notifications
* Notification status

### API Gateway

Acts as the **single entry point** for all client requests.

### Eureka Server

Provides **service discovery** between the microservices.

# 4. Microservice Identification and Service Discovery

The application is divided into independent microservices according to their responsibilities.

| Service              | Port |
| -------------------- | ---: |
| Complaint Service    | 2026 |
| Assignment Service   | 2027 |
| Notification Service | 2028 |
| Auth Service         | 2029 |
| API Gateway          | 8890 |
| Eureka Server        | 8761 |

We use **Eureka Server** as the service registry.
Each microservice registers itself with Eureka, allowing services to discover and communicate with each other using their service names.

For example:

```text
Complaint Service
       ↓
Assignment Service
       ↓
Notification Service
```

This provides **service discovery and loose coupling** between the services.

# 5. JWT Authentication

The system uses **JWT (JSON Web Token)** for authentication.
The three roles first register and then log in:

```text
User Sign-up
Admin Sign-up
Staff Sign-up
       ↓
     Login
       ↓
JWT Token Generated
```

Each logged-in user receives a JWT containing information such as:

* User ID
* Username
* Role
* Expiration

The JWT is sent with protected API requests using:

```text
Authorization: Bearer <JWT>
```
# 6. Authorization

The system uses **role-based authorization** to control access to resources.

### User

The User JWT allows the user to:

* Create complaints
* View their own complaints
* View their own notifications
* Track their complaint progress

A user cannot access another user's complaints or notifications.

### Admin

The Admin JWT allows the Admin to:

* View complaints from all users
* Review complaint details
* Assign complaints to the appropriate department
* Assign complaints to the appropriate staff member
* Manage complaint assignments

### Staff

The Staff JWT allows the Staff member to:

* View complaints assigned specifically to them
* Work on their assigned complaints
* Update the complaint status
* Mark the complaint as resolved

# 7. API Gateway Configuration

The **API Gateway runs on port 8890** and acts as the single entry point to the backend.
The Gateway routes requests according to their endpoint.

| Endpoint            | Service              |
| ------------------- | -------------------- |
| `/user/**`          | Auth Service         |
| `/complaints/**`    | Complaint Service    |
| `/assignments/**`   | Assignment Service   |
| `/notifications/**` | Notification Service |

The Gateway also performs **JWT validation** before forwarding protected requests.

### Gateway Flow

```text
Client
   ↓
API Gateway :8890
   ↓
JWT Validation
   ↓
Eureka Service Discovery
   ↓
Required Microservice
```
# 8. Inter-Service Communication

The microservices communicate with each other through **REST-based communication**.

### Complaint → Assignment

When a User creates a complaint, the Complaint Service communicates with the Assignment Service.

```text
Complaint Service
       ↓
Assignment Service
```

The complaint is then assigned to the appropriate department and staff member.

### Assignment → Notification

After the assignment, the Assignment Service communicates with the Notification Service.

```text
Assignment Service
       ↓
Notification Service
       ↓
User Notification
```

This allows the User to receive updates regarding the complaint.

# 9. Database

The project uses **PostgreSQL** for storing application data.
Each microservice has its own database:

```text
Auth Service          → auth_db
Complaint Service     → complaint_db
Assignment Service    → assignment_db
Notification Service  → notification_db
```

This follows the **Database-per-Service** approach.

# 10. API Testing

**Postman** is used to test the backend REST APIs.
All external API requests are sent through the API Gateway:

```text
http://localhost:8890
```

### Authentication APIs

```text
POST /user/signup
POST /user/login
```

### Complaint APIs

```text
POST /complaints
GET  /complaints
GET  /complaints/my
PUT  /complaints/{id}/status
```

### Assignment APIs

```text
POST /assignments
GET  /assignments
GET  /assignments/my
PUT  /assignments/{id}
```

### Notification APIs

```text
POST /notifications
GET  /notifications/my
GET  /notifications/my/unread
PUT  /notifications/{id}/read
```

---

# 11. Architecture

```text
                         CLIENT
                            |
                            ↓
                   API GATEWAY :8890
                            |
          +-----------------+-----------------+
          |                 |                 |
          ↓                 ↓                 ↓
    AUTH SERVICE      COMPLAINT SERVICE   ASSIGNMENT SERVICE
       :2029               :2026               :2027
                                                  |
                                                  ↓
                                         NOTIFICATION SERVICE
                                                :2028

                    EUREKA SERVER :8761
                     SERVICE DISCOVERY
```

The architecture separates each major responsibility into an independent microservice while allowing the services to communicate through service discovery and REST APIs.

# 12. Project Conclusion

The **Enterprise Issue Escalation & Service Desk Management System** provides a structured and secure way to manage complaints.

The system follows the complete process:

```text
User raises complaint
        ↓
Admin reviews and assigns
        ↓
Staff handles the complaint
        ↓
Staff updates the status
        ↓
User receives notifications
        ↓
Complaint is resolved
```

The project demonstrates the practical implementation of **Microservices, Service Discovery, JWT Authentication, Role-Based Authorization, API Gateway, REST-based Inter-Service Communication, and PostgreSQL database management**.

By separating authentication, complaints, assignments, and notifications into independent services, the system provides a clear and organized architecture for complaint management.

**Our goal is to ensure that every complaint is properly assigned, tracked, updated, and resolved without leaving any issue unanswered.**

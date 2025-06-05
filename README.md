# README

## Table of Contents
1. [Install Docker](#1-install-docker)  
2. [Clone the Project](#2-clone-the-project)  
3. [Run Docker Compose](#3-run-docker-compose)  
4. [Accessing Application](#4-accessing-application)
5. [Postman Collection](#5-postman-collection)
---

## 1. Install Docker

- **Windows**  
  - Enable WSL 2:  
    https://docs.microsoft.com/windows/wsl/install  
  - Docker Desktop:  
    https://www.docker.com/products/docker-desktop

- **macOS**  
  - Docker Desktop:  
    https://www.docker.com/products/docker-desktop

- **Linux**  
  - Docker Engine:  
    https://docs.docker.com/engine/install/

---

## 2. Clone the Project
 ```bash
git clone https://github.com/Cevs/vehicle-registration-service.git
cd vehicle-registration-service
```

---

## 3. Run Docker Compose
docker-compose up -d

---

## 4. Accessing Application
[http://localhost:8080](http://localhost:8080)

## 5. Postman collection
Postman colleciton can be found under root folder. Feel free to import and use it. <br/>
Global variables for **username** and **user_password** are configured <br/>
Only **username** has to be defined. <br/>
**user_password** variable will be automatically populated via script after successful user registration

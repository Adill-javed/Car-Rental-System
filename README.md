# Car Rental System (Console-Based Java Application)

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)

## Overview
A console-based **Car Rental System** in Java that allows users to:

- View available cars
- Check car availability
- Estimate rental costs
- Rent cars and update their availability

The system uses **MySQL** as the database and **JDBC** for connectivity.

---

## Features
- Interactive console menu for users
- Rental cost estimation based on days
- Real-time availability check
- Updates database when a car is rented

---

## Technologies
- Java (JDK 8+)
- MySQL
- JDBC (Java Database Connectivity)

---

## Database Setup

1. Create the database and table:

```sql
CREATE DATABASE car_rental;

USE car_rental;

CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    base_price_per_day DOUBLE NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

INSERT INTO cars (name, base_price_per_day, available) VALUES
('Toyota Corolla', 50, TRUE),
('Honda Civic', 60, TRUE),
('Ford Mustang', 100, TRUE);

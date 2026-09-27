-- PlugPro Admin Database Schema (MySQL)

CREATE DATABASE IF NOT EXISTS plugpro_db;
USE plugpro_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(128) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    role ENUM('customer', 'provider', 'admin') DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Providers Table
CREATE TABLE IF NOT EXISTS providers (
    id VARCHAR(128) PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL,
    name VARCHAR(255) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    category_id VARCHAR(64) NOT NULL,
    experience_years INT DEFAULT 1,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    rating DECIMAL(3, 2) DEFAULT 5.0,
    review_count INT DEFAULT 0,
    completed_jobs INT DEFAULT 0,
    verified_status ENUM('pending', 'verified', 'rejected') DEFAULT 'pending',
    is_available BOOLEAN DEFAULT TRUE,
    about TEXT,
    service_area VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Service Categories Table
CREATE TABLE IF NOT EXISTS services (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    starting_price DECIMAL(10, 2) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    id VARCHAR(128) PRIMARY KEY,
    customer_id VARCHAR(128) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    provider_id VARCHAR(128) NOT NULL,
    provider_name VARCHAR(255) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    booking_date VARCHAR(64) NOT NULL,
    time_slot VARCHAR(64) NOT NULL,
    address TEXT NOT NULL,
    problem_description TEXT,
    service_fee DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('Pending', 'Confirmed', 'Accepted', 'On The Way', 'Started', 'Completed', 'Cancelled', 'Rejected') DEFAULT 'Pending',
    payment_status ENUM('Pending', 'Completed') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Reviews Table
CREATE TABLE IF NOT EXISTS reviews (
    id VARCHAR(128) PRIMARY KEY,
    booking_id VARCHAR(128) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    provider_id VARCHAR(128) NOT NULL,
    rating DECIMAL(2, 1) NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

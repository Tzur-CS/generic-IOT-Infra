# Generic IoT Infrastructure Project

## Overview
The **Generic IoT Infrastructure Project** is a learning-oriented software development project designed to improve skills in backend and full-stack development. The system provides a robust infrastructure to handle IoT device registration, company registration, and updates using multiple communication protocols.

## Features
- **Device & Company Registration**: Register IoT devices and companies through a web interface.
- **Multi-Protocol Communication**:
    - HTTP for web-based interactions
    - TCP for persistent connections
    - UDP for lightweight communication
- **Custom Implementations**:
    - Custom-built **ThreadPool** for efficient request handling
    - Structured request processing with a command factory pattern
- **Database Support**:
    - **MySQL** for structured data storage
    - **NoSQL** for flexible and scalable data handling

## Technologies Used
- **Backend**: Java, Apache Tomcat, Maven
- **Frontend**: HTML, CSS, JavaScript
- **Databases**: MySQL, NoSQL
- **Protocols**: HTTP, TCP, UDP

## Setup & Installation
### Prerequisites
- Java 21
- Apache Tomcat
- MySQL database
- Maven

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/Generic-IoT-Infrastructure.git
   cd Generic-IoT-Infrastructure
   ```
2. Configure the database in `application.properties`.
3. Build the project using Maven:
   ```sh
   mvn clean install
   ```
4. Deploy the application to Tomcat.
5. Start the server and access the web interface.

## Usage
- Send HTTP requests for device/company registration.
- Use TCP/UDP clients to interact with the system.
- Monitor and manage devices via the web interface.

## Contribution
This project is a work in progress. Feel free to fork and improve it!

## License
[MIT License](LICENSE)


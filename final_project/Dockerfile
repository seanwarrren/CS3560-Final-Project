# Use a base JDK image
FROM openjdk:17-slim

# Create app directory inside consider
WORKDIR /app

# Copy all Java source files and libraries
COPY src/main/java /app

# Compile Java files, including external JAR
RUN javac -cp ".:/app/lib/json-20210307.jar" com/budgetapp/WebServer.java

# Expose the port your WebServer listens on
EXPOSE 8080

# Run the backend server
CMD ["java", "-cp", ".:/app/lib/json-20210307.jar", "com.budgetapp.WebServer"]
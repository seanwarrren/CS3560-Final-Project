FROM openjdk:17-slim

# Create working directory
WORKDIR /app

# Copy source code and dependencies
COPY src/main/java /app
COPY lib/json-20210307.jar /app/lib/json-20210307.jar

# Compile all .java files recursively
RUN find . -name "*.java" > sources.txt && \
    javac -cp ".:/app/lib/json-20210307.jar" @sources.txt

# Expose port (optional)
EXPOSE 8080

# Run the server
CMD ["java", "-cp", ".:/app/lib/json-20210307.jar", "com.budgetapp.WebServer"]
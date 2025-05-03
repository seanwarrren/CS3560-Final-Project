FROM openjdk:17-slim

# Set working directory
WORKDIR /app

# Copy source code and jar dependencies
COPY src/main/java /app
COPY src/main/java/lib/json-20210307.jar /app/lib/json-20210307.jar

# Compile Java source into classes/ directory
RUN mkdir -p /app/classes && \
    find . -name "*.java" > sources.txt && \
    javac -d /app/classes -cp ".:/app/lib/json-20210307.jar" @sources.txt

# Set runtime command
CMD ["java", "-cp", "/app/classes:/app/lib/json-20210307.jar", "com.budgetapp.WebServer"]
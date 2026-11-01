FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxxf86vm1 \
    libgl1 \
    && rm -rf /var/lib/apt/lists/*

RUN mvn clean package

CMD ["mvn", "javafx:run"]
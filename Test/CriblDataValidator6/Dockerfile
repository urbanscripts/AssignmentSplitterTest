FROM gradle:jdk18
WORKDIR /app
COPY build.gradle .
#COPY src/ ./src/
COPY . .
RUN gradle build
CMD gradle test

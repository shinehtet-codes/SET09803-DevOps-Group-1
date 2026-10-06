# Use the latest MySQL image
FROM mysql/mysql-server:latest

# Set the working directory
WORKDIR /tmp

# Copy the world database SQL file
COPY world.sql /tmp/

# Execute the SQL file when the MySQL container starts
COPY world.sql /docker-entrypoint-initdb.d/

# Set the root password
ENV MYSQL_ROOT_PASSWORD example
ENV MYSQL_ROOT_HOST=%
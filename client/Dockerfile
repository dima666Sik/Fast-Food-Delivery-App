# Base image with Node.js
FROM node:18 AS build

# Set working directory in container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy all code into container
COPY . .

# Create build for production
RUN npm run build

# Base image for Nginx
FROM nginx:alpine

# Copy build to Nginx directory
COPY --from=build /app/build /usr/share/nginx/html

# Open port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
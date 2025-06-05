FROM node:lts-alpine AS build-stage

WORKDIR /app

# Copy package.json (and lockfile) & install deps
COPY package*.json ./
RUN npm install

# Copy the rest of the source and build
COPY . .
RUN npm run build

#
# Stage 2: copy build output + custom nginx.conf into an Nginx image
#
FROM nginx:stable-alpine AS run-stage

# Remove default nginx.conf (optional, but ensures no accidental defaults remain)
RUN rm /etc/nginx/nginx.conf

# Copy your custom nginx.conf into /etc/nginx/
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built assets from build-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Expose port 80 (Nginx inside container will listen here)
EXPOSE 80

# Start Nginx in foreground
CMD ["nginx", "-g", "daemon off;"]

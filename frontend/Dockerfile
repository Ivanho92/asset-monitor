FROM node:24 AS build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:24-slim
WORKDIR /app
RUN npm install -g http-server
COPY --from=build /app/dist/frontend/browser ./dist
EXPOSE 4200
CMD ["http-server", "dist", "-p", "4200"]

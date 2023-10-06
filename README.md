# FullStack-E-Bookstore

This is a full stack E bookstore created for better user experiences.

## How to run:

### Vue Client (Frontend):
Prerequisite: Make sure you have Node.js and npm (Node Package Manager) installed. You can check by typing node -v and npm -v in your terminal.

1.  Navigate to your Vue project directory:
  cd path-to-your-vue-project

2. Install the necessary dependencies if you haven't already:
  npm install


3.  Run the Vue app:
  npm run serve

By default, your Vue app will be available at http://localhost:8080 (unless you have configured a different port).

### Server (Backend):
The steps to run your server depend on the technology and the setup you're using. Here's a generic guideline for an Express.js server:

1.  Navigate to your server project directory:
  cd path-to-your-server-project

2. Install the necessary dependencies if you haven't already:
  npm install

3.  Run the server:
  npm start
Note: If you've set up scripts differently in your package.json, you might have a different command to start your server.

By default, if you're using Express.js and haven't specified a port, your server will likely run on http://localhost:3000.

Important Points:
Ensure that both your client and server are not trying to run on the same port.
If your frontend needs to communicate with the backend, make sure you've set up CORS (Cross-Origin Resource Sharing) on your server if they are on different origins.
Always check the documentation or README of your projects as there might be specific instructions or scripts to run them.
If you have a more specific setup or encounter any issues, please let me know!

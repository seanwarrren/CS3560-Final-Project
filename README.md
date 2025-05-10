# Project Title: My Budget Assistant
### A React + Java backend web app that helps users track income, expenses, and upcoming bills and provides AI driven spending advice.  
> This app was built using React on the frontend, leveraging functional components, hooks for state management, and Recharts for interactive graphs. The backend uses a Java-based HttpServer that exposes REST endpoints for user, transaction, and date handling.
> Cohere's text-generation API is used to provide AI-driven spending advice, with all data stored in-memory and served over HTTP.
> Finally, the frontend and backend are containerized with Docker and deployed using Render.

 
## Team Members
- **Sean Warren**: Full Stack - designed UI + program flow and implemented all backend services.

## Project Description
My Budget Assistant is a personal finance dashboard that lets users:
1. Log in with a simple name-based flow
2. Add and delete transactions (expenses, income, upcoming bills) via modal dialogs
3. View lists of all spending, income, and upcoming bills
4. Visualize trends over time with a line chart plotting income vs. expenses
5. See spending category breakdowns in a pie chart
6. Ask an AI assistant for personalized spending advice based on user-entered data

## How to Run
The entire application is deployed on Render. Open the following link:
> https://budgetapp-eto1.onrender.com
     
  No local install or build is required. The entire frontend and backend is running.  
*_If dashboard does not load after login, press cmd+shift+r to force reload the page and retry login._

## Features Implemented
- Name-based login/logout
- In-memory, per-user transaction storage so each user has their own isolated spending list
- Add/delete expenses, income, bills
- Line chart of cumulative income vs. expenses using Recharts
- Pie chart of expense categories
- Modal dialogs for adding new entries
- AI Assistant integration via Cohere API
- CORS Support
- Dockerfile for easy deployment
- Fully deployed on Render with automated builds and updates
  
## Future Work
- Utilize a database for persistent storage of user transaction data
- Implement user authentication
- Adjust for mobile compatibility
- Export data to a spreadsheet
- Enhance quality of AI responses

## Known Issues/Bugs/Limitations
- Since all data is stored in memory, the data is lost as soon as the user logs out
- Generation of AI response can be slow
- After login, a blank screen may appear; must hard reload to see dashboard

## Overview + Demonstration Video
Watch here ↓
> https://drive.google.com/file/d/17rq8BUhKxkIZptViV2yrIs_9br7WVQr-/view?usp=sharing  
> Link to Slides: https://drive.google.com/file/d/1wgJJm2o3KSVEhQ2gFiIQsKeL26OHK75n/view?usp=sharing 

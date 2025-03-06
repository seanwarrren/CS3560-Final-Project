# CS3560-Final-Project

## **AI Powered Budgeting Assistant**

### _Description:_

* Develop a web application that helps users track their income and expenses, analyze spending habits, and receive AI-driven budgeting suggestions.
  
### _Key Features:_

* **Income and Expense Tracking:** Users can log transactions and categorize expenses.
  
* **Interactive Financial Reports:** Displays charts and graphs that show an overview of spending habits.
  
* **Spending Prediction:** Predicts future expenses based on past trends.
  
* **AI Budgeting Advice:** Uses OpenAI API to provide personalized spending recommendations.

### _OOP Principles Applied:_

* **Encapsulation:** Protects financial data by handling it securely within backend services.
  
* **Inheritance:** Define a general Transaction Class with specialized classes like IncomeTransaction and ExpenseTransaction.
  
* **Polymorphism:** Allows different transaction types to have unique processing logic.
  
* **Abstraction:** Uses interfaces for transaction operations, allowing flexibilty in operations.

### _Technologies:_

* **GUI:** React for a dynamic user interface.
  
* **Database:** PostgreSQl for storing transactions, budgets, user data, etc.
  
* **API:** OpenAI API for generating financial recommendations.
  
  	* May use Plaid API to automatically import transactions from bank accounts, credit cards, etc.

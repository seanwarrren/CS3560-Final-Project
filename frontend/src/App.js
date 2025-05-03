import React from 'react';
import './App.css';
import { useState, useEffect } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell, Legend
} from 'recharts';
import { flushSync } from 'react-dom'

function App() {

  // ======= Set up a login page on app start =======
  const [loggedIn, setLoggedIn] = useState(false); 
  const [userName, setUserName] = useState(""); 
  const [inputName, setInputName] = useState(""); 
  const [justLoggedIn, setJustLoggedIn] = useState(false); 
  const [isLoggingIn, setIsLoggingIn] = useState(false); 
  const [showLogout, setShowLogout] = useState(false); 
  const [isLoggingOut, setIsLoggingOut] = useState(false);

  useEffect(() => {
    if (!loggedIn) {
      localStorage.removeItem("userName");
    }
  }, [loggedIn]);

  const handleLogin = () => {
    if (inputName.trim() !== "") {
      setIsLoggingIn(true);
      const trimmedName = inputName.trim();

      fetch("http://localhost:8080/api/create-user", {
        method: "POST",
        headers: {
          "Content-Type": "text/plain",
        },
        body: trimmedName,
      })
        .then((res) => {
          if (res.ok) {
            setTimeout(() => {
              setUserName(trimmedName);
              setLoggedIn(true);
              setJustLoggedIn(true);
              setIsLoggingIn(false);
              localStorage.setItem("userName", trimmedName); 
            }, 400);
          } else {
            console.error("Failed to create user");
          }
        })
        .catch((err) => {
          console.error("Server error during login:", err);
        });
    }
  };

  useEffect(() => {
    if (justLoggedIn) {
      const timer = setTimeout(() => setJustLoggedIn(false), 500); 
      return () => clearTimeout(timer);
    }
  }, [justLoggedIn]);


  // ======= Handle user logout =======
  const handleLogout = () => {
    const confirmed = window.confirm("If you logout, data will be lost. Continue?");
    if (!confirmed) return;

    setIsLoggingOut(true);

    setTimeout(() => {
      fetch("http://localhost:8080/api/delete-user", {
        method: "DELETE",
      })
        .then((res) => {
          if (res.ok) {
            setLoggedIn(false);
            setUserName("");
            localStorage.removeItem("userName");
            setAssistantPrompt("");
            setAssistantResponse("");
          } else {
            console.error("Failed to delete user");
          }
        })
        .catch((err) => {
          console.error("Server error during logout:", err);
        })
        .finally(() => {
          setIsLoggingOut(false);
        });
    }, 400); 
  };


  // ======= Modals for adding income, spending, upcoming bills =======
  const [modalOpen, setModalOpen] = useState(false);
  const [modalType, setModalType] = useState("")

  const openModal = (type) => {
    setModalType(type);
    setModalOpen(type);
  };

  const closeModal = () => {
    setModalOpen(false);
    setModalType("");
  };

  const getOptionsForType = () => {
    if (modalType === "Spending") {
      return (
        <>
          <option value="">Select type</option>
          <option value="Food">Food</option>
          <option value="Rent">Rent</option>
          <option value="Entertainment">Entertainment</option>
          <option value="Transportation">Transportation</option>
          <option value="Personal">Personal</option>
        </>
      );
    } else if (modalType === "Income") {
      return (
        <>
          <option value="">Select type</option>
          <option value="Salary">Salary</option>
          <option value="Payment">Payment</option>
          <option value="Investment">Investment</option>
        </>
      );
    } else if (modalType === "Bills") {
      return (
        <>
          <option value="">Select type</option>
          <option value="Rent">Rent</option>
          <option value="Subscriptions">Subscriptions</option>
          <option value="Utilities">Utilities</option>
        </>
      );
    } else {
      return (
        <>
          <option value="">Select type</option>
        </>
      );
    }
  };


  // ======= Capture values from adding spending, income, and upcoming bills =======
  const [transactionName, setTransactionName] = useState("");
  const [transactionAmount, setTransactionAmount] = useState("");
  const [transactions, setTransactions] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("");

  // Derived totals
  const spendingTotal = transactions
    .filter(t => t.type === "EXPENSE")
    .reduce((sum, t) => sum + t.amount, 0)
    .toFixed(2);

  const incomeTotal = transactions
    .filter(t => t.type === "INCOME")
    .reduce((sum, t) => sum + t.amount, 0)
    .toFixed(2);

  const handleSubmitTransaction = () => {
    const name = transactionName.trim();
    const category = selectedCategory.trim();
    const amount = parseFloat(transactionAmount);
    if (!category || isNaN(amount)) return;

    const type = modalType === "Income" 
      ? "INCOME" 
      : modalType === "Bills"
      ? "BILL" 
      : "EXPENSE";

    fetch("http://localhost:8080/api/add-transaction", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ transactionName, category, amount, type }),
    })
      .then((res) => {
        if (res.ok) {
          fetchTransactions();
          closeModal();
          setTransactionName("");
          setTransactionAmount("");
        }
      })
      .catch((err) => console.error("Failed to add transaction", err));
  };

  const fetchTransactions = () => {
    fetch("http://localhost:8080/api/transactions")
      .then((res) => res.json())
      .then((data) => setTransactions(data))
      .catch((err) => console.error("Failed to fetch transactions", err));
  };

  const deleteTransaction = (id) => {
    fetch(`http://localhost:8080/api/transactions/delete?id=${id}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (res.ok) {
          fetchTransactions(); 
        } else {
          console.error("Failed to delete transaction");
        }
      })
      .catch((err) => console.error("Error deleting transaction", err));
  };


  // ======= Transactions =======
  const totalIncome = transactions.filter(t => t.type === "INCOME").reduce((sum, t) => sum + t.amount, 0);
  const totalExpense = transactions.filter(t => t.type === "EXPENSE").reduce((sum, t) => sum + t.amount, 0);
  const netTotal = totalIncome - totalExpense;


  // ======= Load on startup =======
  useEffect(() => {
    if (loggedIn) fetchTransactions();
  }, [loggedIn]);


  // ======= Displaying the date =======
  const [currentDate, setCurrentDate] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/date')
      .then(response => response.text())
      .then(date => setCurrentDate(date))
      .catch(error => console.error('Error fetching date:', error));
  }, []);


  // ======= Charts =======
  const getLineChartData = () => {
    let income = 0;
    let expense = 0;
  
    return transactions.map((t, index) => {
      if (t.type === "INCOME") income += t.amount;
      else if (t.type === "EXPENSE") expense += t.amount;
  
      return {
        name: `#${index + 1}`,
        Income: income,
        Expenses: expense
      };
    });
  };
  
  const getPieChartData = () => {
    const categoryTotals = {};
  
    transactions
      .filter(t => t.type === "EXPENSE")
      .forEach(t => {
        categoryTotals[t.category] = (categoryTotals[t.category] || 0) + t.amount;
      });
  
    return Object.entries(categoryTotals).map(([category, value]) => ({
      name: category,
      value
    }));
  };


  // ======= ChatBot =======
  const [assistantPrompt, setAssistantPrompt] = useState("");
  const [assistantResponse, setAssistantResponse] = useState("");
  const [isAssistantLoading, setIsAssistantLoading] = useState(false);

  const formatTransactionsForPrompt = () => {
    console.log("Transactions array:", transactions);

    if (transactions.length === 0) return "No transaction data available.";
  
    const expenseSummary = transactions
      .filter(t => t.type === "EXPENSE")
      .reduce((acc, t) => {
        acc[t.category] = (acc[t.category] || 0) + t.amount;
        return acc;
      }, {});
  
    const incomeTotal = transactions
      .filter(t => t.type === "INCOME")
      .reduce((sum, t) => sum + t.amount, 0)
      .toFixed(2);
  
    const expenseBreakdown = Object.entries(expenseSummary)
      .map(([category, amount]) => `${category}: $${amount.toFixed(2)}`)
      .join(", ");
  
    return `Income: $${incomeTotal}. Spending by category: ${expenseBreakdown}.`;
  };

  const handleAssistantSubmit = () => {
    if (!assistantPrompt.trim()) return;
  
    setIsAssistantLoading(true);

    const context = formatTransactionsForPrompt();
    const fullPrompt = `
    Pretend you are a financial assistant helping users understand their spending habits. 
    Analyze the following summary and identify 2 to 3 specific patterns or areas where the user might consider adjusting their spending. 
    Frame your observations gently using phrases like "you might consider" or "it could help to..." 
    Do not include disclaimers or general advice.

    Here is the summary:
    ${context}
    `;

    console.log("Prompt being sent to backend:", fullPrompt);
  
    fetch("http://localhost:8080/api/chatbot", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ prompt: fullPrompt }),
    })
      .then(res => {
        if (!res.ok) throw new Error("Bad response from server");
        return res.text();
      })
      .then(data => {
        console.log("Full response from AI:", data);
        flushSync(() => setAssistantResponse(""));
        simulateStreamingText(data);
      })
      .catch(err => {
        console.error("Error fetching AI response:", err);
        setAssistantResponse("Sorry, something went wrong.");
      })
      .finally(() => setIsAssistantLoading(false));
  };

  const simulateStreamingText = (text) => {
    let index = 0;
    setAssistantResponse("");
  
    const typeChar = () => {
      if (index < text.length) {
        setAssistantResponse(text.slice(0, index));
        index++;
        setTimeout(typeChar, 20); 
      }
    };
  
    typeChar();
  };

  return (
    <div className="App">
      {!loggedIn && (
        <div className={`login-page ${isLoggingIn ? 'fade-scale-out' : ''}`}>
          <h1>Welcome to My Budget Assistant</h1>
          <input
            type="text"
            placeholder="Enter your name"
            value={inputName}
            onChange={(e) => setInputName(e.target.value)}
            className="login-input"
          />
          <button onClick={handleLogin} className="login-button">Login</button>
        </div>
      )}
      <>
        {/* Show dashboard */}
        {loggedIn && (
        <div className={`Background ${justLoggedIn ? "fade-scale-in" : ""} ${isLoggingOut ? "fade-scale-out" : ""}`}>
          <div className="box-container">

            {/* Title Box */}
            <div className="title-box">
              <div className="title">My Budget Assistant</div>
              <div className="subtitle">Welcome, {userName.split(" ")[0]}</div>
            </div>

            {/* All boxes */}
            {/* Box 1: Spending list */}
            <div className="box1">
              <span className="box1-title">Spending List</span>
              <ul className="transaction-list">
              {transactions
                .filter(t => t.type === "EXPENSE")
                .map(({ id, name, category, amount }) => (
                  <li className="transaction-entry" key={id}>
                    <div className="transaction-text">
                      <div className="transaction-name">{name}: ${amount.toFixed(2)}</div>
                      <div className="transaction-category">{category}</div>
                    </div>
                    <button className="delete-btn" onClick={() => deleteTransaction(id)}>×</button>
                  </li>
                ))}
              </ul>
            </div>

            {/* Box 3: Income and Expenses Graph */}
            <div className="box3">
              <span className='box3-title'>Income and Expenses</span>
              <ResponsiveContainer width="95%" height="85%">
                <LineChart data={getLineChartData()}>
                  <CartesianGrid stroke="rgba(255, 255, 255, 0.1)" strokeDasharray="3 3" />
                  <XAxis
                    dataKey="name"
                    tick={{ fill: "#FFFFFF", fontFamily: "Lexend", fontSize: 12 }}
                    axisLine={{ stroke: "#FFFFFF" }}
                    tickLine={{ stroke: "#FFFFFF" }}
                  />
                  <YAxis
                    tick={{ fill: "#FFFFFF", fontFamily: "Lexend", fontSize: 12 }}
                    axisLine={{ stroke: "#FFFFFF" }}
                    tickLine={{ stroke: "#FFFFFF" }}
                  />
                  <Tooltip
                    contentStyle={{ backgroundColor: "#1D1F3A", border: "none", borderRadius: 8, color: "#fff" }}
                    labelStyle={{ color: "#fff", fontFamily: "Lexend" }}
                    itemStyle={{ color: "#fff", fontFamily: "Lexend" }}
                  />
                  <Legend wrapperStyle={{ fontSize: 12, fontFamily: "Lexend", color: "#FFFFFF" }} />
                  <Line type="monotone" dataKey="Income" stroke="#03E8CD" strokeWidth={2} />
                  <Line type="monotone" dataKey="Expenses" stroke="#FC6E4B" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </div>

            {/* Box 4: Spending categories breakdown */}
              <div className="box4">
                <span className="box4-title">Spending Categories Breakdown</span>
                <ResponsiveContainer width="100%" height="85%">
                  <PieChart>
                    <Pie
                      data={getPieChartData()}
                      dataKey="value"
                      nameKey="name"
                      cx="50%"
                      cy="50%"
                      outerRadius={80}
                    >
                      {getPieChartData().map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={['#FC6E4B', '#DC1F6E', '#03E8CD', '#FFA500', '#8B8B8B'][index % 5]} />
                      ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                  </PieChart>
                </ResponsiveContainer>
              </div>

            {/* Box 5: AI assistant */}
            <div className="box5">
              <span className="box5-title">AI Assistant</span>
              <div className="ai-assistant">
                <div className="assistant-response-box">
                {isAssistantLoading ? (
                  <div className="typing-indicator">
                    <span></span><span></span><span></span>
                  </div>
                ) : (
                  assistantResponse && <p>{assistantResponse}</p>
                )}
                </div>
                <div className="chat-input-container">
                  <input
                    className="assistant-input"
                    type="text"
                    placeholder="Type your question..."
                    value={assistantPrompt}
                    onChange={(e) => setAssistantPrompt(e.target.value)}
                  />
                  <button className="chat-submit" onClick={handleAssistantSubmit}>
                    ▶
                  </button>
                </div>
              </div>
            </div>

            {/* Box 6: Add Transaction */}
            <div className="box6">
              <span className='box6-title'>Spending</span>
              <button className="text-button6" onClick={() => openModal("Spending")}>+</button>
              <div className="box-total">${spendingTotal}</div>
            </div>

            {/* Box 7: Add Income */}
            <div className="box7">
              <span className='box7-title'>Income</span>
              <button className="text-button7" onClick={() => openModal("Income")}>+</button>
              <div className="box-total">${incomeTotal}</div>
            </div>

            {/* Box 8: Income list */}
            <div className="box8">
              <span className="box8-title">Income List</span>
              <ul className="transaction-list">
                {transactions
                  .filter(t => t.type === "INCOME") 
                  .map(({ id, name, category, amount }) => (
                    <li className="transaction-entry" key={id}>
                      <div className="transaction-text">
                        <div className="transaction-name">{name}: ${amount.toFixed(2)}</div>
                        <div className="transaction-category">{category}</div>
                      </div>
                      <button className="delete-btn" onClick={() => deleteTransaction(id)}>×</button>
                    </li>
                  ))}
              </ul>
            </div>

            {/* Box 9: Upcoming bills */}
            <div className="box9">
              <span className="box9-title">Upcoming Bills</span>
              <button className="text-button9" onClick={() => openModal("Bills")}>+</button>
              <ul className="transaction-list">
                {transactions
                  .filter(t => t.type === "BILL")
                  .map(({ id, name, category, amount }) => (
                    <li className="transaction-entry" key={id}>
                      <div className="transaction-text">
                        <div className="transaction-name">{name}: ${amount.toFixed(2)}</div>
                        <div className="transaction-category">{category}</div>
                      </div>
                      <button className="delete-btn" onClick={() => deleteTransaction(id)}>×</button>
                    </li>
                  ))}
              </ul>
            </div>

            {/* Box 10: Net total */}
            <div className="box10">
              <div className="net-total-text">Net Total: ${netTotal.toFixed(2)}</div>
            </div>

            {/* Box 11: Date */}
            <div className="box11">
              <div className="date-text">Today: {currentDate}</div>
            </div>

            {/* User info */}
            <div 
              className="user-info-wrapper"
              onMouseEnter={() => setShowLogout(true)}
              onMouseLeave={() => setShowLogout(false)}
            >
              <div className="user-name">{userName}</div>
              <div className="circle" onClick={handleLogout}>
                {userName.charAt(0).toUpperCase()}
              </div>

              {showLogout && (
                <div className="logout-tooltip">Logout</div>
              )}
            </div>

          </div>
        </div>
        )}
        {modalOpen && (
          <div className="modal-overlay">
            <div className="modal modal-animate">
              <h2>Add {modalType}</h2>
              <input 
                type="text" 
                placeholder="Enter name" 
                className="modal-input" 
                value={transactionName}
                onChange={(e) => setTransactionName(e.target.value)}
              />
              <input
                type="number"
                placeholder="Enter amount"
                className="modal-input"
                value={transactionAmount}
                onChange={(e) => setTransactionAmount(e.target.value)}
              />
              <select 
                className="modal-select"
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
              >
                {getOptionsForType()}
              </select>
              <button className="modal-submit" onClick={handleSubmitTransaction}>
                Submit
              </button>
              <button className="modal-close" onClick={closeModal}>
                Close
              </button>
            </div>
          </div>
        )}
      </>
    </div>
  );
}

export default App;
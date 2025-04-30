import React from 'react';
import './App.css';
import { useState, useEffect } from 'react';

function App() {

  // ======= Set up a login page on app start =======
  const [loggedIn, setLoggedIn] = useState(false);
  const [userName, setUserName] = useState("");
  const [inputName, setInputName] = useState("");
  const [justLoggedIn, setJustLoggedIn] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);

  const handleLogin = () => {
    if (inputName.trim() !== "") {
      setIsLoggingIn(true);
      setTimeout(() => {
        setUserName(inputName.trim());
        setLoggedIn(true);
        setJustLoggedIn(true);
        setIsLoggingIn(false);
        localStorage.setItem("userName", inputName);
      }, 400); // match duration of fade-out
    }
  };

  useEffect(() => {
    const savedName = localStorage.getItem("userName");
    if (savedName) {
      setUserName(savedName);
      setLoggedIn(true);
    }
  }, []);

  useEffect(() => {
    if (justLoggedIn) {
      const timer = setTimeout(() => setJustLoggedIn(false), 500); // matches animation length
      return () => clearTimeout(timer);
    }
  }, [justLoggedIn]);

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
          <option value="Utilities">Utilities</option>
          <option value="Subscriptions">Subscriptions</option>
          <option value="Loans">Loans</option>
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

  // ======= Displaying the date using backend ======
  const [currentDate, setCurrentDate] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/date')
      .then(response => response.text())
      .then(date => setCurrentDate(date))
      .catch(error => console.error('Error fetching date:', error));
  }, []);

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
        {/* show dashboard */}
        {loggedIn && (
        <div className={`Background ${justLoggedIn ? "fade-scale-in" : ""}`}>
          <div className="box-container">

            {/* Title Box */}
            <div className="title-box">
              <div className="title">My Budget Assistant</div>
              <div className="subtitle">Welcome, {userName.split(" ")[0]}</div>
            </div>

            {/* All boxes */}
            {/* Box 1: Spending list */}
            <div className="box1">
              <text className="box1-title">Spending List</text>
            </div>

            {/* Box 3: Income and Expenses Graph */}
            <div className="box3">
              <text className='box3-title'>Income and Expenses</text>
            </div>

            {/* Box 4: Spending categories breakdown */}
            <div className="box4">
              <text className="box4-title">Spending Categories Breakdown</text>
            </div>

            {/* Box 5: AI assistant */}
            <div className="box5">
              <text className='box5-title'>AI Assistant</text>
            </div>

            {/* Box 6: Add Transaction */}
            <div className="box6">
              <text className='box6-title'>Spending</text>
              <button className="text-button6" onClick={() => openModal("Spending")}>+</button>
            </div>

            {/* Box 7: Add Income */}
            <div className="box7">
              <text className='box7-title'>Income</text>
              <button className="text-button7" onClick={() => openModal("Income")}>+</button>
            </div>

            {/* Box 8: Income list */}
            <div className="box8">
              <text className="box8-title">Income List</text>
            </div>

            {/* Box 9: Upcoming bills */}
            <div className="box9">
              <text className="box9-title">Upcoming Bills</text>
              <button className="text-button9" onClick={() => openModal("Bills")}>+</button>
            </div>

            {/* Box 10: Net total */}
            <div className="box10">
              <div className="net-total-text">Net Total: </div>
            </div>

            {/* Box 11: Date */}
            <div className="box11">
              <div className="date-text">Today: {currentDate}</div>
            </div>

            {/* User info */}
            <div className="user-info-wrapper">
              <div className="user-name">{userName}</div>
              <div className="circle">{userName.charAt(0).toUpperCase()}</div>
            </div>

          </div>
        </div>
        )}
        {modalOpen && (
          <div className="modal-overlay">
            <div className="modal modal-animate">
              <h2>Add {modalType}</h2>
              <input type="text" placeholder="Enter name" className="modal-input" />
              <select className="modal-select">
                {getOptionsForType()}
              </select>
              <button className="modal-submit">Submit</button>
              <button className="modal-close" onClick={closeModal}>Close</button>
            </div>
          </div>
        )}
      </>
    </div>
  );
}

export default App;
import React from 'react';
import './App.css';

function App() {
  return (
    <div className="App">
      <div className="Background">
        <div className="box-container">

          {/* Title Box */}
          <div className="title-box">
            <div className="title">My Budget Assistant</div>
            <div className="subtitle">Available Balance</div>
            <div className="subtitle" style={{ color: '#03E8CD', fontSize: '29px' }}>$123,456</div>
          </div>

          {/* All boxes */}
          <div className="box1"></div>
          <div className="box2"></div>
          {/* Box 3: Income and Expenses Graph */}
          <div className="box3">
          <text className='box3-title'>Income and Expenses</text>
          </div>
          <div className="box4"></div>
          {/* Box 5: AI assistant */}
          <div className="box5">
            <text className='box5-title'>AI Assistant</text>
          </div>
          {/* Box 6: Add Transaction */}
          <div className="box6">
            <text className='box6-title'>Spending</text>
            <button className="text-button6">+</button>
          </div>
          {/* Box 7: Add Income */}
          <div className="box7">
            <text className='box7-title'>Income</text>
            <button className="text-button7">+</button>
          </div>
          <div className="box8"></div>
          <div className="box9"></div>
          <div className="box10"></div>
          <div className="box11"></div>

          {/* Circle */}
          <div className="circle"></div>

        </div>
      </div>
    </div>
  );
}

export default App;
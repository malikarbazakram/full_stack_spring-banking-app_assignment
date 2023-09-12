import React, { useState } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const TransactionForm = () => {
  const [amount, setAmount] = useState(0);
  const [transactionType, setTransactionType] = useState('Credit'); // Default to add funds

  const handleAmountChange = (event) => {
    setAmount(event.target.value);
  };

  const handleTransactionTypeChange = (event) => {
    setTransactionType(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await axios.post(
        `/api/v1/transactions/${transactionType}`,
        null,
        {
          params: { amount: parseFloat(amount) },
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa('admin:admin'),
          },
        }
      );
      if (response.status === 200) {
        // Add a delay before displaying the toast (e.g., 2 seconds)
        setTimeout(() => {
          // Display a success toast notification
          toast.success("Transaction successful!", {
            position: toast.POSITION.BOTTOM_RIGHT,
            onClose: () => {
              // Add a delay before navigating to the home route (e.g., 1 second)
              setTimeout(() => {
                // Navigate to the home route ("/") after the toast is closed
                window.location.href = '/'; // You can use any navigation method you prefer
              }, 2000); // 1 second delay
            }
          });
        }, 500); 
      } else {
        // Show an error toast notification
        toast.error('Transaction failed. Funds not available.', {
          position: toast.POSITION.BOTTOM_RIGHT,
        });
      }
    } catch (error) {
      // Handle errors and show an error toast notification
      toast.error('Transaction failed. Funds not available.', {
        position: toast.POSITION.BOTTOM_RIGHT,
      });
    }
  };

  return (
    
    <div className="container">
      <br/><br/>
      <div className="row">
        <div className="col-md-6 offset-md-3">
          <div className="card">
            <div className="card-body">
              
              <h1 className="card-title"><b>Transaction Form</b></h1>
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="amount" className="form-label">
                    <b>Amount :</b>
                  </label>
                  <input
                    type="number"
                    min="1"
                    id="amount"
                    name="amount"
                    value={amount}
                    onChange={handleAmountChange}
                    className="form-control"
                    required
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="transactionType" className="form-label">
                    <b>Transaction Type :</b>
                  </label>
                  <select
                    id="transactionType"
                    name="transactionType"
                    value={transactionType}
                    onChange={handleTransactionTypeChange}
                    className="form-select"
                  >
                    <option value="Credit">Add Funds</option>
                    <option value="Debit">Withdraw Funds</option>
                  </select>
                </div>
                <button type="submit" className="btn btn-dark">
                  Confirm
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
      <ToastContainer />
    </div>
  );
};

export default TransactionForm;
import React, { useState } from "react";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from 'react-router-dom';
// import { Navigate } from "react-router-dom";

const TransferFunds = () => {
  const [receiverAccountId, setreceiverAccountId] = useState("");
  const [amount, setAmount] = useState(0);
  const navigate = useNavigate();

  const handlereceiverAccountIdChange = event => {
    setreceiverAccountId(event.target.value);
  };

  const handleAmountChange = event => {
    setAmount(event.target.value);
  };

  const handleSubmit = async event => {
    event.preventDefault();

    // Check if the amount is greater than 100
    if (parseFloat(amount) <= 0) {
      // Show an error toast notification
      toast.error("Amount must be greater than 0", {
        position: toast.POSITION.BOTTOM_RIGHT
      });
      return; // Exit the function if the amount is not valid
    }

    try {
      const response = await axios.post(
        "/api/v1/transactions/transferfunds",
        null,
        {
          params: {
            amount: parseFloat(amount),
            receiverAccountId: receiverAccountId
          },
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            Authorization: "Basic " + btoa("admin:admin")
          }
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
        toast.error("Transaction failed. Something went wrong.", {
          position: toast.POSITION.BOTTOM_RIGHT
        });
      }

      console.log("Transaction successful:", response.data);
    } catch (error) {
      // Handle errors and show an error toast notification
      toast.error("Transaction failed. Something went wrong.", {
        position: toast.POSITION.BOTTOM_RIGHT
      });
      console.error("Error making transaction:", error);
    }
  };

  return (
    <div className="container">
      <br />
      <br />
      <div className="row">
        <div className="col-md-6 offset-md-3">
          <div className="card">
            <div className="card-body">
              <h1 className="card-title">
                <b>Transfer Funds Form</b>
              </h1>
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="text" className="form-label">
                    <b>Receiver's Account Id :</b>
                  </label>
                  <input
                    type="text"
                    id="receiverAccountId"
                    name="receiverAccountId"
                    value={receiverAccountId}
                    onChange={handlereceiverAccountIdChange}
                    className="form-control"
                    required
                  />
                </div>
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
                <button type="submit" className="btn btn-dark">
                  Transfer
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

export default TransferFunds;

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AiTwotoneDelete, AiTwotoneEdit } from 'react-icons/ai';
import AddAccountForm from '../Account/AddAccountForm';
import EditAccountForm from '../Account/EditAccountForm';

const AccountTable = ({ accounts, onDelete, onEdit }) => {
  const handleDelete = async (id) => {
    try {
      // Send a DELETE request to the API endpoint to delete the account
      await axios.delete(`/api/v1/accounts/${id}`, {
        withCredentials: true,
        headers: {
          'Authorization': 'Basic ' + btoa('admin:admin'),
        },
      });

      // Call the onDelete function to remove the deleted account from the UI
      onDelete(id);

      // Show a success toast notification
      toast.success('Account deleted successfully!', {
        position: toast.POSITION.BOTTOM_RIGHT,
      });
    } catch (error) {
      // Show an error toast notification
      toast.error('Error deleting account. Please try again.', {
        position: toast.POSITION.BOTTOM_RIGHT,
      });

      console.error('Error deleting account:', error);
    }
  };

  return (
    <table className="table table-hover shadow">
      <thead className='thead-dark'>
        <tr class="table-dark" style={{ textAlign: 'center' }}>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Address</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {accounts.map((account) => (
          <tr key={account.id} style={{ verticalAlign: 'middle', textAlign: 'center' }}>
            <th class="table-secondary"
              style={{ textAlign: 'center' }}
            > {account.id}</th>
            <td>{account.name}</td>
            <td>{account.email}</td>
            <td>{account.address}</td>
            <td>
              <button className='btn' onClick={() => onEdit(account)}>
                <h4><AiTwotoneEdit /></h4>
              </button>
              <button className='btn' onClick={() => handleDelete(account.id)}>
                <h4><AiTwotoneDelete /></h4>
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

const ViewAccounts = () => {
  const [accounts, setAccounts] = useState([]);
  const [editingAccount, setEditingAccount] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false); // Add this state

  useEffect(() => {
    loadAccounts();
  }, []);

  const loadAccounts = async () => {
    try {
      const response = await axios.get('/api/v1/accounts/all', {
        withCredentials: true,
        headers: {
          'Authorization': 'Basic ' + btoa('admin:admin'),
        },
      });

      // Filter out the "admin" account from the response data
      const filteredAccounts = response.data.filter(
        (account) => account.name !== 'admin'
      );

      setAccounts(filteredAccounts);
    } catch (error) {
      console.error('Error loading accounts:', error);
    }
  };

  const handleDeleteAccount = (id) => {
    // Update the accounts state by filtering out the deleted account
    setAccounts((prevAccounts) =>
      prevAccounts.filter((account) => account.id !== id)
    );
  };

  const handleAddNewAccount = () => {
    // Show the add account form when the button is clicked
    setShowAddForm(true);
  };

  // Add this function to handle adding the new account to the list
  const handleAddAccount = (newAccount) => {
    setAccounts([...accounts, newAccount]);
    setShowAddForm(false); // Hide the add account form

    // // Show a success toast notification
    // toast.success('Account added successfully!', {
    //   position: toast.POSITION.BOTTOM_RIGHT,
    // });
  };

  const handleEditAccount = (account) => {
    // Set the account being edited and show the edit form
    setEditingAccount(account);
    setShowEditForm(true);
  };

  const handleSaveEditedAccount = (editedAccount) => {
    // Update the accounts state with the edited account
    setAccounts((prevAccounts) =>
      prevAccounts.map((account) =>
        account.id === editedAccount.id ? editedAccount : account
      )
    );

    // Show a success toast notification
    toast.success('Account updated successfully!', {
      position: toast.POSITION.BOTTOM_RIGHT,
    });
  };

  const handleCloseEditForm = () => {
    // Reset the editingAccount and hide the edit form
    setEditingAccount(null);
    setShowEditForm(false);
  };
  const handleCloseCreateForm = () => {
    // Hide the add account form
    setShowAddForm(false);
  };

  return (
    <div className="container">
      <h1 style={{ color: 'white' }}> Account List</h1>
      <br />
      <button
        type="button"
        className="btn btn-dark"
        onClick={handleAddNewAccount}>
        Add New Account
      </button>
      <br />
      <br />
      {/* {showAddForm && ( // Render the add account form when showAddForm is true
      <AddAccountForm onAdd={handleAddAccount}  onClose={handleCloseCreateForm} />

        // <AddAccountForm onAdd={handleAddAccount} />
      )} */}
      {showAddForm && (
        <AddAccountForm onAdd={handleAddAccount} onClose={handleCloseCreateForm} />
      )}
      {showEditForm && (
        <EditAccountForm
          account={editingAccount}
          onSave={handleSaveEditedAccount}
          onClose={handleCloseEditForm}
        />
      )}
      <AccountTable
        accounts={accounts}
        onDelete={handleDeleteAccount}
        onEdit={handleEditAccount}
      />
      <ToastContainer />
    </div>
  );
};

export default ViewAccounts;
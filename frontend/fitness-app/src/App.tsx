import { Box, Button } from "@mui/material"
import { useContext, useEffect, useState } from "react"
import { AuthContext, type IAuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux"
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router"
import { logout, setCredentials } from "./store/authSlice"
import { ActivityForm } from "./components/ActivityForm"
import { ActivityList } from "./components/ActivityList"
import { ActivityDetail } from "./components/ActivityDetail"
import "./App.css"

const ActivitiesPage = () => {
  const { logOut } = useContext<IAuthContext>(AuthContext);
  const dispatch = useDispatch();

  const handleLogout = () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
    dispatch(logout());
    logOut();
  };

  return (
    <div className="fitness-page">
      <div className="fitness-header">
        <div>
          <p className="eyebrow">Workout tracker</p>
          <h1>My fitness dashboard</h1>
        </div>
        <Button className="ghost-button" variant="contained" color="secondary" onClick={handleLogout}>
          Logout
        </Button>
      </div>
      <Box component="section" className="fitness-panel">
        <ActivityForm onActivityAdded = {() => window.location.reload()} />
        <ActivityList />
      </Box>
    </div>
  );
}

function App() {
  const {token, tokenData, logIn, loginInProgress } = useContext<IAuthContext>(AuthContext)
  const dispatch = useDispatch();

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({token, user: tokenData}));
    }
  }, [token, tokenData, dispatch]);

  if (loginInProgress) return <div>Loading...</div>

  return (
    <Router>
      {!token ? (
        <div className="auth-screen">
          <div className="auth-card">
            <p className="eyebrow">Stay active</p>
            <h2>Welcome back</h2>
            <p>Track workouts, stay consistent, and keep your momentum going.</p>
            <Button className="primary-button" variant="contained"
              onClick={() => {
                logIn();
              }}>Login</Button>
          </div>
        </div>
      ) : (
        <Box component="section" className="fitness-shell">
          <Routes>
            <Route path="/activities" element={<ActivitiesPage/>}/>
            <Route path="/" element={<Navigate to="/activities" replace />}/>
            <Route path="activities/:id" element={<ActivityDetail />}/>
          </Routes>
        </Box>
      )}
    </Router>
  )
}

export default App

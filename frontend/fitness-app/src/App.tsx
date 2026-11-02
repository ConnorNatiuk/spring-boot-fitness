import { Button } from "@mui/material"
import { useContext, useEffect, useState } from "react"
import { AuthContext, type IAuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux"
import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from "react-router"
import { setCredentials } from "./store/authSlice"

function App() {
  const {token, tokenData, logIn, logOut, loginInProgress } = useContext<IAuthContext>(AuthContext)
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({token, user: tokenData}));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  if (loginInProgress) return <div>Loading...</div>
  if (!token) return <div>Redirecting to login...</div>

  return (
    <Router>
      <Button variant="contained" sx={{ bgcolor: "dc004e" }}>LOGIN</Button>
      
    </Router>
  )
}

export default App

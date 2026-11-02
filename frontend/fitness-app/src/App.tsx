import { Box, Button } from "@mui/material"
import { useContext, useEffect, useState } from "react"
import { AuthContext, type IAuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux"
import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from "react-router"
import { setCredentials } from "./store/authSlice"
import { ActivityForm } from "./components/ActivityForm"
import { ActivityList } from "./components/ActivityList"
import { ActivityDetail } from "./components/ActivityDetail"

const ActivitiesPage = () => {
  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm onActivityAdded = {() => window.location.reload()} />
      <ActivityList />
    </Box>
  );
}

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
  if (!authReady) return <div>Redirecting to login...</div>

  return (
    <Router>
      {!token ? (
        <Button variant="contained" sx={{ bgcolor: "dc004e" }}
          onClick={() => {
            logIn();
          }}>LOGIN</Button>
        ) : (
          <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
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

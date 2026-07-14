import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { useAuth } from './Components/hooks/AuthContext'
import { MessageProvider } from './Components/alerts/MessageContext'
import Auth from './Components/Auth/Auth'
import OAuthCallBack from './Components/Auth/OAuthCallBack'
import AuthenticationSuccess from './Components/Auth/AuthenticationSuccess'
import DashBoard from './Components/dashboard/DashBoard'

function App() {
  const { isLoggedIn } = useAuth()

  return (
    <MessageProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={isLoggedIn ? <DashBoard /> : <Auth />} />
          <Route path="/call-back" element={<AuthenticationSuccess />} />
          <Route path="/authenticate" element={<OAuthCallBack />} />
          <Route path="/dashboard" element={isLoggedIn ? <DashBoard /> : <Auth />} />
        </Routes>
      </BrowserRouter>
    </MessageProvider>
  )
}

export default App

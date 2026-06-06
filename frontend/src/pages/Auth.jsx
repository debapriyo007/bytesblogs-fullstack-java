import * as React from "react"
import { useNavigate } from "react-router-dom"
import { useAuth } from "@/context/AuthContext"
import { useToast } from "@/context/ToastContext"
import { api } from "@/services/api"

import OtpVerification from "@/components/auth/OtpVerification"
import AuthForm from "@/components/auth/AuthForm"

export default function Auth({ isModal = false, onClose, initialMode = "login" }) {
  const { login, register, verifyOtp, token } = useAuth()
  const toast = useToast()
  const navigate = useNavigate()
  const [mode, setMode] = React.useState(initialMode)
  const [loading, setLoading] = React.useState(false)

  // Form Fields
  const [username, setUsername] = React.useState("")
  const [email, setEmail] = React.useState("")
  const [password, setPassword] = React.useState("")
  const [showPassword, setShowPassword] = React.useState(false)

  // OTP Verification States & Refs
  const [verificationStep, setVerificationStep] = React.useState(false)
  const [verificationEmail, setVerificationEmail] = React.useState("")
  const [otp, setOtp] = React.useState(Array(6).fill(""))
  const [resendCooldown, setResendCooldown] = React.useState(0)
  const [resending, setResending] = React.useState(false)
  const inputRefs = React.useRef([])

  React.useEffect(() => {
    // If already logged in, handle callback or redirect
    if (token) {
      if (isModal) {
        onClose?.()
      } else {
        navigate("/")
      }
    }
  }, [token, navigate, isModal, onClose])

  React.useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const err = params.get("error")
    if (err) {
      toast.error(decodeURIComponent(err))
      // Clean up URL so the query parameter doesn't linger in address bar
      window.history.replaceState({}, document.title, window.location.pathname)
    }
  }, [toast])

  React.useEffect(() => {
    let timer
    if (resendCooldown > 0) {
      timer = setTimeout(() => setResendCooldown((prev) => prev - 1), 1000)
    }
    return () => clearTimeout(timer)
  }, [resendCooldown])

  React.useEffect(() => {
    if (verificationStep) {
      const timer = setTimeout(() => {
        inputRefs.current[0]?.focus()
      }, 50)
      return () => clearTimeout(timer)
    }
  }, [verificationStep])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      if (mode === "login") {
        if (!email || !password) throw new Error("Please fill in all fields")
        await login(email, password)
        toast.success("Login successful!")
        if (isModal) {
          onClose?.()
        } else {
          navigate("/")
        }
      } else {
        if (!username || !email || !password) throw new Error("Please fill in all fields")
        if (username.length < 3) throw new Error("Username must be at least 3 characters")
        if (password.length < 6) throw new Error("Password must be at least 6 characters")
        
        await register(username, email, password)
        toast.success("Registration successful! A 6-digit verification code has been sent to your email.")
        setVerificationEmail(email)
        setVerificationStep(true)
      }
    } catch (err) {
      if (err.status === 403) {
        setVerificationEmail(email)
        setVerificationStep(true)
        toast.warning(err.message || "Account not verified. Please enter the OTP code sent to your email.")
      } else {
        toast.error(err.message || "An unexpected error occurred. Please try again.")
      }
    } finally {
      setLoading(false)
    }
  }

  const handleOtpChange = (index, value) => {
    if (value && !/^\d$/.test(value)) return

    const newOtp = [...otp]
    newOtp[index] = value
    setOtp(newOtp)

    if (value && index < 5) {
      inputRefs.current[index + 1]?.focus()
    }
  }

  const handleOtpKeyDown = (index, e) => {
    if (e.key === "Backspace") {
      if (!otp[index] && index > 0) {
        const newOtp = [...otp]
        newOtp[index - 1] = ""
        setOtp(newOtp)
        inputRefs.current[index - 1]?.focus()
      } else {
        const newOtp = [...otp]
        newOtp[index] = ""
        setOtp(newOtp)
      }
    }
  }

  const handleOtpPaste = (e) => {
    e.preventDefault()
    const pastedData = e.clipboardData.getData("text").trim()
    if (!/^\d{6}$/.test(pastedData)) return

    const newOtp = pastedData.split("")
    setOtp(newOtp)
    inputRefs.current[5]?.focus()
  }

  const handleVerifyOtp = async (e) => {
    if (e) e.preventDefault()
    const code = otp.join("")
    if (code.length < 6) {
      toast.error("Please enter all 6 digits of the verification code")
      return
    }

    setLoading(true)
    try {
      await verifyOtp(verificationEmail, code)
      toast.success("Verification successful! You are now signed in.")
      if (isModal) {
        onClose?.()
      } else {
        navigate("/")
      }
    } catch (err) {
      toast.error(err.message || "Invalid or expired OTP. Please try again.")
    } finally {
      setLoading(false)
    }
  }

  const handleResendOtp = async () => {
    if (resendCooldown > 0 || resending) return
    setResending(true)
    try {
      await api.post(`/api/auth/resend-otp?email=${encodeURIComponent(verificationEmail)}`)
      toast.success("A new verification code has been dispatched to your email.")
      setResendCooldown(60)
      setOtp(Array(6).fill(""))
      setTimeout(() => {
        inputRefs.current[0]?.focus()
      }, 50)
    } catch (err) {
      toast.error(err.message || "Failed to resend verification code. Please try again.")
    } finally {
      setResending(false)
    }
  }

  const toggleMode = () => {
    setMode((prev) => (prev === "login" ? "register" : "login"))
    setUsername("")
    setEmail("")
    setPassword("")
  }

  const handleGoogleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google"
  }

  if (verificationStep) {
    return (
      <OtpVerification
        email={verificationEmail}
        otp={otp}
        loading={loading}
        resending={resending}
        resendCooldown={resendCooldown}
        inputRefs={inputRefs}
        onOtpChange={handleOtpChange}
        onOtpKeyDown={handleOtpKeyDown}
        onOtpPaste={handleOtpPaste}
        onVerify={handleVerifyOtp}
        onResend={handleResendOtp}
        onBack={() => {
          setVerificationStep(false)
          setOtp(Array(6).fill(""))
        }}
        isModal={isModal}
      />
    )
  }

  return (
    <AuthForm
      mode={mode}
      toggleMode={toggleMode}
      username={username}
      setUsername={setUsername}
      email={email}
      setEmail={setEmail}
      password={password}
      setPassword={setPassword}
      showPassword={showPassword}
      setShowPassword={setShowPassword}
      loading={loading}
      onSubmit={handleSubmit}
      onGoogleLogin={handleGoogleLogin}
      isModal={isModal}
    />
  )
}

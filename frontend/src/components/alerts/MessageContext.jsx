import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import styled, { keyframes } from 'styled-components';

const MessageContext = createContext();

const slideUp = keyframes`
  from { opacity: 0; transform: translateY(16px); }
  to   { opacity: 1; transform: translateY(0); }
`;

// severity -> colour. reusing the same red/amber the PR status badges already use.
const severityColors = {
	success: '#10b981',
	error: '#ff6a6a',
	warning: '#f59e0b',
	info: '#0b6cff',
};

const Toast = styled.div`
  position: fixed;
  left: 1.5rem;
  bottom: 1.5rem;
  z-index: 9999;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  min-width: 260px;
  max-width: 420px;
  padding: 0.9rem 1rem;
  border-radius: 12px;
  background: ${({ $severity }) => severityColors[$severity] || severityColors.info};
  color: #fff;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 0.98rem;
  font-weight: 500;
  box-shadow: 0 12px 30px rgba(11, 44, 77, 0.18);
  animation: ${slideUp} 0.28s cubic-bezier(0.23, 1, 0.32, 1);

  @media (max-width: 600px) {
    left: 0.7rem;
    right: 0.7rem;
    max-width: none;
  }
`;

const ToastText = styled.span`
  flex: 1;
`;

const CloseButton = styled.button`
  background: transparent;
  border: none;
  border-radius: 50%;
  padding: 0.2rem;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: background 0.18s;

  &:hover {
    background: rgba(255, 255, 255, 0.18);
  }
  svg path {
    stroke: #fff;
  }
`;

export const MessageProvider = ({ children }) => {
	// one toast at a time — a new message just replaces whatever's currently up
	const [toast, setToast] = useState(null);

	const displayMessage = useCallback((message, messageType = 'info') => {
		setToast({ message, messageType });
	}, []);

	const handleClose = useCallback(() => setToast(null), []);

	// clears itself after 3s. re-runs on each new toast so the timer restarts rather
	// than an old one cutting the new message short.
	useEffect(() => {
		if (!toast) return;
		const timer = setTimeout(() => setToast(null), 3000);
		return () => clearTimeout(timer);
	}, [toast]);

	return (
		<MessageContext.Provider value={{ displayMessage }}>
			{children}
			{toast && (
				<Toast $severity={toast.messageType} role="alert" aria-live="polite">
					<ToastText>{toast.message}</ToastText>
					<CloseButton onClick={handleClose} aria-label="Dismiss" title="Dismiss">
						<svg width="16" height="16" fill="none" viewBox="0 0 24 24">
							<path d="M18 6L6 18M6 6l12 12" strokeWidth="2" strokeLinecap="round" />
						</svg>
					</CloseButton>
				</Toast>
			)}
		</MessageContext.Provider>
	);
};

export const useMessage = () => {
	return useContext(MessageContext);
};

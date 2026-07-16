import {
  AuthWrapper,
  LoginFormContainer,
  LogoImg,
  GithubButton,
  GithubIcon
} from './styles/Auth.styles';
import logo from '../../assets/smartmerge_logo.svg';

function Auth() {
  const handleLogin = () => {
    window.location.href = `${import.meta.env.VITE_API_URL}/oauth2/authorization/github`;
  };

  return (
    <AuthWrapper>
      <LoginFormContainer>

        <LogoImg src={logo} alt="SmartMerge Logo" />

        <GithubButton onClick={handleLogin}>
          <GithubIcon />
          Login with GitHub
        </GithubButton>

      </LoginFormContainer>
    </AuthWrapper>
  );
}

export default Auth

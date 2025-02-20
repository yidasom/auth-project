import React, { useState } from 'react';

const Login: React.FC = () => {
  const [username, setUsername] = useState('');
  const handleLogin = () => alert(`로그인 시도: ${username}`);

  return (
    <div>
      <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
      <button onClick={handleLogin}>로그인</button>
    </div>
  );
};

export default Login;

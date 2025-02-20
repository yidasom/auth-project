import { useNavigate } from "react-router-dom";
// import { useAuthStore } from "../stores/authStore";

const Home = () => {
  const navigate = useNavigate();
//   const { isAuthenticated, logout } = useAuthStore();


  if (!isAuthenticated) {
    navigate("/login");
    return null;
  }

  return (
    <div>
      <h2>홈 페이지</h2>
//       <button onClick={logout}>로그아웃</button>
    </div>
  );
};

export default Home;

import { Link, Outlet } from 'react-router-dom';

import githubIcon from "../../assets/github.png";

export function AppRoute() {
  return (
    <div className="route-app">
      <header>
        <a className="icon" href="https://github.com/josephgarnett/advent-of-code">
          <img src={githubIcon} alt="Github link" />
        </a>
        <h1>Advent of code 2023</h1>
        <nav>
          <ul className="app-navigiation">
            <li>
              <Link to="/2022">2022</Link>
            </li>
            <li>
              <Link to="/2023">2023</Link>
            </li>
          </ul>
        </nav>
      </header>
      <Outlet/>
    </div>
  );
}
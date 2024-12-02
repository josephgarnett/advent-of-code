import { NavLink, Outlet } from 'react-router-dom';

import githubIcon from "../../assets/github.png";

import './app.scss';

export function AppRoute() {
  return (
    <div className="route-app">
      <header>
        <a className="icon" href="https://github.com/josephgarnett/advent-of-code">
          <img src={githubIcon} alt="Github link" />
        </a>
        <h1 className="app-header">
          Advent of code
          <nav className="app-navigation">
            <ul>
              <li>
                <NavLink to="/2024">2024</NavLink>
              </li>
              /
              <li>
                <NavLink to="/2023">2023</NavLink>
              </li>
              /
              <li>
                <NavLink to="/2022">2022</NavLink>
              </li>
            </ul>
          </nav>
        </h1>
      </header>
      <Outlet/>
    </div>
  );
}
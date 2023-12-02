import { ApiProvider } from '@reduxjs/toolkit/query/react';

import githubIcon from './assets/github.png';
import { api } from './services/api';
import {ReportView} from "./features/report";

function App() {
  return (
    <ApiProvider api={api}>
        <header>
          <a className="icon" href="https://github.com/josephgarnett/advent-of-code">
            <img src={githubIcon} alt="Github link" />
          </a>
          <h1>Advent of code 2023</h1>
        </header>
        <ReportView year="2023" />
    </ApiProvider>
  );
}

export default App

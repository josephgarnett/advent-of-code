import { RouterProvider } from "react-router-dom";
import { ApiProvider } from '@reduxjs/toolkit/query/react';

import { api } from './services/api';
import { router } from './routes';

function App() {
  return (
    <ApiProvider api={api}>
      <RouterProvider router={router} />
    </ApiProvider>
  );
}

export default App

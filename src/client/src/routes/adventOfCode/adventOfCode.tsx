import { Navigate, useParams } from "react-router-dom";

import { ReportView } from "../../features/report";

export function AdventOfCodeRoute() {
  const { year } = useParams();

  if (!year) {
    return (
      <Navigate to="/2023" replace />
    );
  }

  return (
    <ReportView year={year}/>
  );
}
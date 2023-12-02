import { useGetReportQuery } from '../../services/api';
import { ReportEntry } from "./ReportEntry";

import './report.scss';

interface ReportViewProps {
  year: '2023';
}
export function ReportView({ year }: ReportViewProps) {
  const { data } = useGetReportQuery({ year });

  return (
    <ul className="report">
      {data?.content.map((entry) => (
        <ReportEntry key={entry.id} {...entry} />
      ))}
    </ul>
  )
}
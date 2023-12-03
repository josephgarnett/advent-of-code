import { useGetReportQuery } from '../../services/api';
import { ReportEntry } from "./ReportEntry";

import './report.scss';

interface ReportViewProps {
  year: string;
}
export function ReportView({ year }: ReportViewProps) {
  const { data, isError } = useGetReportQuery({ year });

  if (isError) {
    return (
      <div>
        {`No data for ${year}`}
      </div>
    )
  }

  return (
    <ul className="report">
      {data?.content.map((entry) => (
        <ReportEntry key={entry.id} {...entry} />
      ))}
    </ul>
  )
}
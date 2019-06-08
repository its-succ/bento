import { format, startOfWeek, endOfWeek } from "date-fns";

export function edgeOfWeek(date) {
  const start = startOfWeek(date);
  const end = endOfWeek(date);
  return { start, end };
}

export function formatDate(date) {
  return format(date, "YYYYMMDD");
}

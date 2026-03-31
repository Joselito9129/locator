export interface IGenericResponseEntity<T> {
  success: boolean;
  message: string;
  data: T;
}
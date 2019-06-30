export class User {
  user_id: number;
  username: string;
  password: string;
  first_name: string;
  last_name: string;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}

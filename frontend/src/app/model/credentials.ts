export class Credentials {
  grant_type = "password";
  Username: string;
  Password: string;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}

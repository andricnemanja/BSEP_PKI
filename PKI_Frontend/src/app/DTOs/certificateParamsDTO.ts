export class CertificateParamsDTO {
  certificateType : String = "";
  notBefore: Date = new Date();
  issuer : number = 0;
  keyUsage : Array<String> = [];
  extendedKeyUsage: Array<String> = [];
  commonName : String = "";
  surname : String = "";
  givenName : String = "";
  organization : String = "";
  organizationUnit : String = "";
  country : String = "";
  email : String = "";
  password : String = "";
}
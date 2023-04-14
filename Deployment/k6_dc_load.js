import { exec } from "k6/x/exec";

//const filePath = "C:\Users\hlalpuriya\OneDrive - DXC Production\Learning\Cribl\Assignment\Docker-compose.yaml"

export default function () {
    let result = exec("docker-compose up agent -d");
    console.log(result.stdout);
    console.error(result.stderr);
  }
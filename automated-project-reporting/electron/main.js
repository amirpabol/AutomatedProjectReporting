const { app, BrowserWindow } = require("electron");
const path = require("path");
const { spawn } = require("child_process");

let mainWindow;
let backendProcess;

app.whenReady().then(() => {
  // 🟡 Start Spring Boot JAR in background
  backendProcess = spawn("java", [
    "-jar",
    "../automated-project-reporting/target/automated-project-reporting.jar",
  ]);

  mainWindow = new BrowserWindow({
    width: 1280,
    height: 800,
    webPreferences: {
      contextIsolation: false,
      nodeIntegration: true,
    },
  });

  mainWindow.loadURL("http://localhost:3000"); // Next.js dev server or exported build

  mainWindow.on("closed", () => {
    if (backendProcess) backendProcess.kill();
  });
});

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

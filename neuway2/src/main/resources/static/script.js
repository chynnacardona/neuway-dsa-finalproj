// ===============================
// IMAGE ROUTE MAP
// ===============================

const displayImageList = {
    "GATEMAINBLG": "Objects/GateMain.png",
    "MAINBLGGATE": "Objects/GateMain.png",

    "GATELIBRARY": "Objects/GateLib.png",
    "LIBRARYGATE": "Objects/GateLib.png",

    "GATESECRETGARDEN": "Objects/GateSega.png",
    "SECRETGARDENGATE": "Objects/GateSega.png",

    "GATESOM": "Objects/GateSOM.png",
    "SOMGATE": "Objects/GateSOM.png",

    "GATEISBLG": "Objects/GateIS.png",
    "ISBLGGATE": "Objects/GateIS.png",

    "GATEPSB": "Objects/GatePSB.png",
    "PSBGATE": "Objects/GatePSB.png",

    "MAINBLGLIBRARY": "Objects/MainLib.png",
    "LIBRARYMAINBLG": "Objects/MainLib.png",

    "MAINBLGISBLG": "Objects/MainIS.png",
    "ISBLGMAINBLG": "Objects/MainIS.png",

    "MAINBLGPSB": "Objects/MainPSB.png",
    "PSBMAINBLG": "Objects/MainPSB.png",

    "MAINBLGSECRETGARDEN": "Objects/MainSega.png",
    "SECRETGARDENMAINBLG": "Objects/MainSega.png",

    "MAINBLGSOM": "Objects/MainSOM.png",
    "SOMMAINBLG": "Objects/MainSOM.png",

    "LIBRARYISBLG": "Objects/LibIS.png",
    "ISBLGLIBRARY": "Objects/LibIS.png",

    "LIBRARYPSB": "Objects/LibPSB.png",
    "PSBLIBRARY": "Objects/LibPSB.png",

    "LIBRARYSECRETGARDEN": "Objects/LibSega.png",
    "SECRETGARDENLIBRARY": "Objects/LibSega.png",

    "LIBRARYSOM": "Objects/LibSOM.png",
    "SOMLIBRARY": "Objects/LibSOM.png",

    "ISBLGPSB": "Objects/ISPSB.png",
    "PSBISBLG": "Objects/ISPSB.png",

    "ISBLGSECRETGARDEN": "Objects/ISSega.png",
    "SECRETGARDENISBLG": "Objects/ISSega.png",

    "ISBLGSOM": "Objects/ISSOM.png",
    "SOMISBLG": "Objects/ISSOM.png",

    "PSBSOM": "Objects/PSBSOM.png",
    "SOMPSB": "Objects/PSBSOM.png"
};

// ===============================
// IMAGE HELPERS
// ===============================

function showImage(imgPath) {
    const imgElement = document.getElementById("displayImage");
    if (imgElement && imgPath) imgElement.src = imgPath;
}

function showDefaultImage() {
    showImage("Objects/NEU.png");
}

function showRouteImage(start, destination) {
    const clean = s => s.toUpperCase().replace(/[^A-Z0-9]/g, "");
    const key = clean(start) + clean(destination);
    const rev = clean(destination) + clean(start);

    if (displayImageList[key]) {
        showImage(displayImageList[key]);
    } else if (displayImageList[rev]) {
        showImage(displayImageList[rev]);
    } else {
        alert("This route does not exist in the map.");
        showDefaultImage();
    }
}

// ===============================
// AUTOSUGGEST
// ===============================

async function fetchSuggestion(inputElement) {
    const value = inputElement.value.trim();
    const listId = inputElement.id === "start"
        ? "suggestions-start"
        : "suggestions-destination";

    const list = document.getElementById(listId);
    list.innerHTML = "";

    if (!value) return;

    try {
        const response = await fetch("/dijkstra/search", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ prefix: value })
        });

        const items = await response.json();

        items.forEach(item => {
            const li = document.createElement("li");
            li.textContent = item;

            li.onclick = () => {
                inputElement.value = item;
                list.innerHTML = "";
            };

            list.appendChild(li);
        });
    } catch (err) {
        console.error("Suggestion error:", err);
    }
}

// ===============================
// MAIN FIND PATH BUTTON
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    document.getElementById("findPath").addEventListener("click", async () => {
        const start = document.getElementById("start").value.trim();
        const destination = document.getElementById("destination").value.trim();

        if (!start || !destination) {
            alert("Enter both location and destination.");
            return;
        }

        // ✅ SHOW IMAGE
        showRouteImage(start, destination);

        try {
            // ✅ FETCH DISTANCE
            const res1 = await fetch("/dijkstra", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ start, destination })
            });

            const data1 = await res1.json();
            document.getElementById("distance").innerHTML =
                `${data1.totalDistance || data1.distance || "XX"} <span style="font-size:14px">meters</span>`;

            // ✅ FETCH OTHER ROUTES
            const res2 = await fetch("/dijkstra/all-paths", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ start, destination })
            });

            const data2 = await res2.json();
            let out = "";
            data2.forEach((route, i) => {
                out += `${i + 1}. ${route.path} | ${route.distance} meters\n`;
            });

            document.getElementById("result2").textContent = out;

        } catch (err) {
            console.error("Path error:", err);
            alert("Failed to find path.");
        }
    });

    // ✅ ELECTRON WINDOW BUTTONS
    const closeBtn = document.getElementById("close");
    const minBtn = document.getElementById("minimize");

    closeBtn.addEventListener("click", () => window.electronAPI.close());
    minBtn.addEventListener("click", () => window.electronAPI.minimize());
});

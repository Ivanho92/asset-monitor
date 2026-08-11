"""
Fake source generator for Asset Monitor.

Simulates several fictional "sources" (vehicles, checkpoints, beacons) that each
periodically report their status. Posts each report to the backend's REST API.

This is a REST client for now, by design -- in Week 3 this file gets swapped to
publish over XMPP instead. Nothing else in the system needs to change when that
happens, since the backend's "receive a report" boundary stays the same either way.

All source names, entity types, and statuses below are entirely fictional/generic.
"""

import os
import random
import time
import logging
from datetime import datetime, timezone

import requests

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
log = logging.getLogger("fake-sources")

BACKEND_URL = os.environ.get("BACKEND_URL", "http://localhost:8080")
REPORTS_ENDPOINT = f"{BACKEND_URL}/api/reports"
INTERVAL_SECONDS = float(os.environ.get("INTERVAL_SECONDS", "5"))

# Fictional sources. Each has a type (which determines what statuses make sense for it)
# and a base location that reports jitter slightly around, to simulate movement/noise.
SOURCES = [
    {"sourceId": "unit-07", "entityType": "vehicle", "base_lat": 50.85, "base_lon": 4.35},
    {"sourceId": "unit-12", "entityType": "vehicle", "base_lat": 50.86, "base_lon": 4.36},
    {"sourceId": "gate-A", "entityType": "checkpoint", "base_lat": 50.855, "base_lon": 4.355},
    {"sourceId": "gate-B", "entityType": "checkpoint", "base_lat": 50.858, "base_lon": 4.342},
    {"sourceId": "beacon-03", "entityType": "beacon", "base_lat": 50.847, "base_lon": 4.368},
]

STATUSES_BY_TYPE = {
    "vehicle": ["OPERATIONAL", "OPERATIONAL", "OPERATIONAL", "OFFLINE", "MALFUNCTION"],
    "checkpoint": ["ASSET_PASSED", "ASSET_PASSED", "IDLE"],
    "beacon": ["ONLINE", "ONLINE", "ONLINE", "OFFLINE"],
}


def jitter(value: float, amount: float = 0.01) -> float:
    return round(value + random.uniform(-amount, amount), 6)


def build_report() -> dict:
    source = random.choice(SOURCES)
    status = random.choice(STATUSES_BY_TYPE[source["entityType"]])
    return {
        "sourceId": source["sourceId"],
        "entityType": source["entityType"],
        "lat": jitter(source["base_lat"]),
        "lon": jitter(source["base_lon"]),
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "status": status,
    }


def send_report(report: dict) -> None:
    try:
        response = requests.post(REPORTS_ENDPOINT, json=report, timeout=5)
        response.raise_for_status()
        log.info("Sent report: %s -> %s (%s)", report["sourceId"], report["status"], response.status_code)
    except requests.exceptions.RequestException as e:
        log.warning("Failed to send report from %s: %s", report["sourceId"], e)


def main() -> None:
    log.info("Starting fake source generator. Posting to %s every %ss", REPORTS_ENDPOINT, INTERVAL_SECONDS)
    while True:
        report = build_report()
        send_report(report)
        time.sleep(INTERVAL_SECONDS)


if __name__ == "__main__":
    main()

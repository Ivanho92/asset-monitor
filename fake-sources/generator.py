"""
Fake source generator for Asset Monitor.

Simulates several fictional "sources" (vehicles, checkpoints, beacons), each running
its own persistent XMPP connection under its own registered account. Each source
periodically sends its status report as a <message> stanza to the backend's JID.

All source names, entity types, and statuses below are entirely fictional/generic.
"""

import asyncio
import json
import logging
import os
import random
from datetime import datetime, timezone

from slixmpp import ClientXMPP

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
log = logging.getLogger("fake-sources")

XMPP_HOST = os.environ.get("XMPP_HOST", "xmpp")
XMPP_PORT = int(os.environ.get("XMPP_PORT", "5222"))
XMPP_DOMAIN = os.environ.get("XMPP_DOMAIN", "localhost")
SOURCE_PASSWORD = os.environ.get("SOURCE_PASSWORD", "changeme")
BACKEND_JID = os.environ.get("BACKEND_JID", f"backend@{XMPP_DOMAIN}")
INTERVAL_SECONDS = float(os.environ.get("INTERVAL_SECONDS", "5"))

# Fictional sources. Each connects as its own registered XMPP account.
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


def build_report(source: dict) -> dict:
    status = random.choice(STATUSES_BY_TYPE[source["entityType"]])
    return {
        "sourceId": source["sourceId"],
        "entityType": source["entityType"],
        "lat": jitter(source["base_lat"]),
        "lon": jitter(source["base_lon"]),
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "status": status,
    }


class SourceClient(ClientXMPP):
    """One persistent XMPP connection for a single fictional source."""

    def __init__(self, source: dict):
        self.source = source
        jid = f"{source['sourceId'].lower()}@{XMPP_DOMAIN}"
        super().__init__(jid, SOURCE_PASSWORD)

        self.enable_starttls = False
        self.enable_direct_tls = False
        self.enable_plaintext = True
        self["feature_mechanisms"].unencrypted_plain = True
        self["feature_mechanisms"].use_mech = "PLAIN"

        self.add_event_handler("session_start", self.on_session_start)
        self.add_event_handler("failed_all_auth", self.on_failed_auth)

    async def on_failed_auth(self, event):
        log.warning("%s: authentication failed, retrying in 5s (account may not be registered yet)", self.source["sourceId"])
        await asyncio.sleep(5)
        self.connect(host=XMPP_HOST, port=XMPP_PORT)

    async def on_session_start(self, event):
        self.send_presence()
        await self.get_roster()
        log.info("%s connected", self.source["sourceId"])
        asyncio.create_task(self.send_loop())

    async def send_loop(self):
        # Random initial delay so all sources don't start sending simultaneously.
        await asyncio.sleep(random.uniform(0, INTERVAL_SECONDS))

        while True:
            report = build_report(self.source)
            self.send_message(mto=BACKEND_JID, mbody=json.dumps(report), mtype="chat")
            log.info("Sent report: %s -> %s", self.source["sourceId"], report["status"])
            # Small jitter on top of the base interval keeps sends from drifting back
            # into sync over time, and reads a bit more like independent sensors.
            await asyncio.sleep(INTERVAL_SECONDS * random.uniform(0.5, 2))


def main() -> None:
    log.info("Starting %d fake sources, connecting to %s:%s", len(SOURCES), XMPP_HOST, XMPP_PORT)

    for source in SOURCES:
        client = SourceClient(source)
        client.connect(host=XMPP_HOST, port=XMPP_PORT)

    asyncio.get_event_loop().run_forever()


if __name__ == "__main__":
    main()

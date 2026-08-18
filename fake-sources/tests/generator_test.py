"""Unit tests for the fake source generator's pure logic (no XMPP connection needed)."""

from generator import build_report, jitter, SOURCES, STATUSES_BY_TYPE


def test_jitter_stays_within_expected_range():
    base = 50.85
    amount = 0.01
    for _ in range(100):
        result = jitter(base, amount)
        assert base - amount <= result <= base + amount


def test_build_report_uses_valid_status_for_entity_type():
    for source in SOURCES:
        report = build_report(source)
        assert report["status"] in STATUSES_BY_TYPE[source["entityType"]]


def test_build_report_includes_source_fields():
    source = SOURCES[0]
    report = build_report(source)

    assert report["sourceId"] == source["sourceId"]
    assert report["entityType"] == source["entityType"]
    assert "timestamp" in report
    assert "lat" in report
    assert "lon" in report

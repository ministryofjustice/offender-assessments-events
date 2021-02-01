    {{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for web and worker containers
*/}}
{{- define "deployment.envs" }}
env:
  - name: SERVER_PORT
    value: "{{ .Values.image.port }}"

  - name: JAVA_OPTS
    value: "{{ .Values.env.JAVA_OPTS }}"

  - name: SPRING_PROFILES_ACTIVE
    value: "oracle,logstash"

  - name: OAUTH_ENDPOINT_URL
    value: "{{ .Values.env.OAUTH_ENDPOINT_URL }}"

  - name: APPINSIGHTS_INSTRUMENTATIONKEY
    valueFrom:
      secretKeyRef:
        name: {{ template "app.name" . }}
        key: APPINSIGHTS_INSTRUMENTATIONKEY

  - name: LAST_ACCESSED_EVENT_DIR
    value: "{{ .Values.env.LAST_ACCESSED_EVENT_DIR}}"

  - name: SNS_AWS_ACCESS_KEY_ID
    valueFrom:
      secretKeyRef:
        name: offender-assessments-events
        key: SNS_AWS_ACCESS_KEY_ID

  - name: SNS_AWS_SECRET_ACCESS_KEY
    valueFrom:
      secretKeyRef:
        name: offender-assessments-events
        key: SNS_AWS_SECRET_ACCESS_KEY

  - name: SNS_TOPIC_ARN
    valueFrom:
      secretKeyRef:
        name: offender-assessments-events
        key: SNS_TOPIC_ARN

  - name: SPRING_DATASOURCE_USERNAME
    valueFrom:
      secretKeyRef:
        name: {{ template "app.name" . }}
        key: SPRING_DATASOURCE_USERNAME

  - name: SPRING_DATASOURCE_PASSWORD
    valueFrom:
      secretKeyRef:
        name: {{ template "app.name" . }}
        key: SPRING_DATASOURCE_PASSWORD

  - name: SPRING_DATASOURCE_URL
    valueFrom:
      secretKeyRef:
        name: {{ template "app.name" . }}
        key: SPRING_DATASOURCE_URL

{{- end -}}

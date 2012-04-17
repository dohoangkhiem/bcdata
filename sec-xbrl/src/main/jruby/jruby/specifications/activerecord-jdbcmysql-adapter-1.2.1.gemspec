# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{activerecord-jdbcmysql-adapter}
  s.version = "1.2.1"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = [%q{Nick Sieger, Ola Bini and JRuby contributors}]
  s.date = %q{2011-11-23}
  s.description = %q{Install this gem to use MySQL with JRuby on Rails.}
  s.email = %q{nick@nicksieger.com, ola.bini@gmail.com}
  s.homepage = %q{https://github.com/jruby/activerecord-jdbc-adapter}
  s.require_paths = [%q{lib}]
  s.rubyforge_project = %q{jruby-extras}
  s.rubygems_version = %q{1.8.9}
  s.summary = %q{MySQL JDBC adapter for JRuby on Rails.}

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<activerecord-jdbc-adapter>, ["~> 1.2.1"])
      s.add_runtime_dependency(%q<jdbc-mysql>, ["~> 5.1.0"])
    else
      s.add_dependency(%q<activerecord-jdbc-adapter>, ["~> 1.2.1"])
      s.add_dependency(%q<jdbc-mysql>, ["~> 5.1.0"])
    end
  else
    s.add_dependency(%q<activerecord-jdbc-adapter>, ["~> 1.2.1"])
    s.add_dependency(%q<jdbc-mysql>, ["~> 5.1.0"])
  end
end
